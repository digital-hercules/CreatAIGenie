# Use the official Python image with the specified version
FROM python:3.10-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Set the working directory in the container
WORKDIR /src

# Copy the requirements file into the container
COPY requirements.txt /src/

RUN pip install gunicorn

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the entire project into the container
COPY . /src/

# Copy the .env file into the container
COPY .env /src/

# Expose the port the app runs on
EXPOSE 8000

# Collect static files
RUN python manage.py collectstatic --noinput

RUN python manage.py makemigrations

RUN python manage.py migrate

CMD ["gunicorn", "--bind", "0.0.0.0:8000", "src.wsgi:application"]

