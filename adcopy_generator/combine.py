import csv
import random
from faker import Faker
import PyPDF2
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import pandas as pd
from openai import OpenAI
import os
from dotenv import load_dotenv
import tempfile
import shutil

load_dotenv()
# Download necessary NLTK data
nltk.download('punkt', quiet=True)
nltk.download('stopwords', quiet=True)

fake = Faker()
client = OpenAI(api_key=os.environ.get("OPENAI_API_KEY"))

def read_csv(filename):
    with open(filename, 'r', newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        return list(reader)

def extract_text_from_pdf(pdf_file):
    with open(pdf_file, 'rb') as file:
        reader = PyPDF2.PdfReader(file)
        text = ""
        for page in reader.pages:
            text += page.extract_text()
    return text

def preprocess_text(text):
    stop_words = set(stopwords.words('english'))
    ps = PorterStemmer()
    
    tokens = word_tokenize(text.lower())
    tokens = [ps.stem(token) for token in tokens if token.isalnum() and token not in stop_words]
    return ' '.join(tokens)

def assign_keywords(products, keywords):
    # Combine all product information into a single string for each product
    product_texts = [' '.join(product.values()) for product in products]
    
    # Preprocess product texts and keywords
    preprocessed_products = [preprocess_text(text) for text in product_texts]
    preprocessed_keywords = [preprocess_text(keyword) for keyword in keywords]
    
    # Create TF-IDF vectors
    vectorizer = TfidfVectorizer()
    product_vectors = vectorizer.fit_transform(preprocessed_products)
    keyword_vectors = vectorizer.transform(preprocessed_keywords)
    
    # Calculate cosine similarity
    similarities = cosine_similarity(product_vectors, keyword_vectors)
    
    # Assign keywords to products
    for i, product in enumerate(products):
        assigned_keywords = set()  # Use a set to avoid duplicates
        confidence_scores = {}
        for j, score in enumerate(similarities[i]):
            if score > 0.1:  # Adjust this threshold as needed
                keyword = keywords[j]
                if keyword not in assigned_keywords:
                    assigned_keywords.add(keyword)
                    confidence_scores[keyword] = score
        
        product['Assigned Keywords'] = ', '.join(assigned_keywords)
        product['Confidence Scores'] = ', '.join([f"{keyword}:{score:.2f}" for keyword, score in confidence_scores.items()])
    
    return products

def generate_product_data(num_products):
    products = []
    for _ in range(num_products):
        try:
            response = client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": "You are a product data generator."},
                    {"role": "user", "content": f"Generate a product with the following details: Product Name, Brand Name, ASIN, Category, Model Number, About Product, Product Specification, Technical Details, Shipping Weight, Product Dimensions. Provide the data in CSV format."}
                ],
                max_tokens=150,
                temperature=0.7
            )
            product_data = response.choices[0].message.content.strip().split(',')
            if len(product_data) == 10:  # Ensure we have all 10 fields
                products.append({
                    'Product Name': product_data[0],
                    'Brand Name': product_data[1],
                    'Asin': product_data[2],
                    'Category': product_data[3],
                    'Model Number': product_data[4],
                    'About Product': product_data[5],
                    'Product Specification': product_data[6],
                    'Technical Details': product_data[7],
                    'Shipping Weight': product_data[8],
                    'Product Dimensions': product_data[9]
                })
        except Exception as e:
            print(f"Error generating product data: {e}")
    return products

def write_to_csv(data, filename):
    temp_file = None
    try:
        # First, try to write directly to the specified file
        with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
            fieldnames = list(data[0].keys())
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(data)
        print(f"Data successfully written to {filename}")
    except PermissionError:
        print(f"Permission denied when writing to {filename}. Trying alternative methods...")
        
        # Method 1: Write to a temporary file
        temp_file = tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.csv', newline='', encoding='utf-8')
        with temp_file as csvfile:
            fieldnames = list(data[0].keys())
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(data)
        print(f"Data written to temporary file: {temp_file.name}")
        
        # Method 2: Try to copy the temp file to the desired location
        try:
            shutil.copy(temp_file.name, filename)
            print(f"Temporary file successfully copied to {filename}")
        except PermissionError:
            print(f"Unable to copy to {filename}. Please manually copy the temporary file.")
        
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
    
    return temp_file.name if temp_file else filename

if __name__ == "__main__":
    input_csv = 'marketing_amazon_com-ecommerce.csv'
    input_pdf = 'bulkkeyword.pdf'
    output_csv = 'output_products_with_keywords.csv'
    
    # Generate additional product data using OpenAI
    num_additional_products = 20  # Adjust as needed
    generated_products = generate_product_data(num_additional_products)
    
    # Read existing products from CSV
    existing_products = read_csv(input_csv)
    
    # Combine existing and generated products
    all_products = existing_products + generated_products
    
    # Extract keywords from PDF
    pdf_text = extract_text_from_pdf(input_pdf)
    keywords = list(set(pdf_text.split()))  # Use a set to remove duplicates
    
    # Assign keywords to products
    products_with_keywords = assign_keywords(all_products, keywords)
    
    # Write results to new CSV
    final_output_file = write_to_csv(products_with_keywords, output_csv)
    
    print(f"Analysis complete. Results saved to {final_output_file}")
    print("If a temporary file was created, please ensure to copy it to your desired location.")