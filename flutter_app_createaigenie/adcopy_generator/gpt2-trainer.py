import os
import json
import csv
import pdfplumber
import docx

from transformers import GPT2LMHeadModel, GPT2Tokenizer, Trainer, TrainingArguments

def load_data(data_path, data_format):
    """
    Load data from the specified file format.
    Supports JSON, CSV, PDF, and DOCX.
    """
    if data_format == "jsonl":
        with open(data_path, "r") as f:
            data = [json.loads(line) for line in f]
    elif data_format == "csv":
        data = []
        with open(data_path, "r") as f:
            reader = csv.DictReader(f)
            for row in reader:
                data.append(row)
    elif data_format == "pdf":
        with pdfplumber.open(data_path) as pdf:
            data = [page.extract_text() for page in pdf.pages]
    elif data_format == "docx":
        doc = docx.Document(data_path)
        data = [paragraph.text for paragraph in doc.paragraphs]
    else:
        raise ValueError(f"Unsupported data format: {data_format}")
    return data

def train_gpt2(train_data, model_name, output_dir, num_epochs, batch_size, learning_rate):
    """
    Train a GPT-2 model using the provided training data.
    """
    model = GPT2LMHeadModel.from_pretrained(model_name)
    tokenizer = GPT2Tokenizer.from_pretrained(model_name)

    train_dataset = tokenizer.encode_plus(
        " ".join(train_data),
        add_special_tokens=True,
        max_length=1024,
        padding="max_length",
        truncation=True,
        return_tensors="pt"
    )

    training_args = TrainingArguments(
        output_dir=output_dir,
        num_train_epochs=num_epochs,
        per_device_train_batch_size=batch_size,
        learning_rate=learning_rate,
        save_steps=500,
        save_total_limit=2,
    )

    trainer = Trainer(
        model=model,
        args=training_args,
        train_dataset=train_dataset,
    )

    trainer.train()
    trainer.save_model(output_dir)

if __name__ == "__main__":
    data_path = "path/to/your/data"
    data_format = "jsonl"  # or "csv", "pdf", "docx"
    model_name = "gpt2"
    output_dir = "path/to/output/directory"
    num_epochs = 3
    batch_size = 8
    learning_rate = 5e-5

    train_data = load_data(data_path, data_format)
    train_gpt2(train_data, model_name, output_dir, num_epochs, batch_size, learning_rate)
