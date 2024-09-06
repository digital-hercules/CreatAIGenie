from django.db import models
from django.conf import settings
from django.utils import timezone

class UserProfile(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    bio = models.TextField(blank=True)
    profile_pic = models.ImageField(upload_to='media/profile_pics/', blank=True)
    website = models.URLField(blank=True)
    location = models.CharField(max_length=255, blank=True)
    birth_date = models.DateField(null=True, blank=True)

    def __str__(self):
        return f'{self.user.username} Profile'

class Document(models.Model):
    user_profile = models.ForeignKey(UserProfile, on_delete=models.CASCADE, related_name='documents')
    title = models.CharField(max_length=255)
    upload = models.FileField(upload_to='media/docs/')
    description = models.TextField(blank=True)
    uploaded_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title

class ProcessedData(models.Model):
    document = models.OneToOneField(Document, on_delete=models.CASCADE, related_name='processed_data')
    summary = models.TextField()
    analysis = models.TextField(blank=True)
    processed_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Processed data for {self.document.title}"

class SubscriptionPlan(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    price = models.DecimalField(max_digits=10, decimal_places=2)
    duration_days = models.PositiveIntegerField()

    def __str__(self):
        return self.name

class UserSubscription(models.Model):
    user_profile = models.ForeignKey(UserProfile, on_delete=models.CASCADE, related_name='subscriptions')
    plan = models.ForeignKey(SubscriptionPlan, on_delete=models.CASCADE, related_name='subscriptions')
    start_date = models.DateTimeField(auto_now_add=True)
    end_date = models.DateTimeField()

    def __str__(self):
        return f"{self.user_profile.user.username} - {self.plan.name}"
    
    def is_active(self):
        return self.start_date <= timezone.now() <= self.end_date


class Signagetemp(models.Model):
    title = models.CharField(max_length=100)
    description = models.TextField(blank=True, null=True)
    signage_image = models.ImageField(upload_to='signage_templates/')
    width = models.IntegerField(default=800)
    height = models.IntegerField(default=1200)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    category = models.ForeignKey('SignageCategory', on_delete=models.SET_NULL, null=True, related_name='signage_templates')

    def __str__(self):
        return self.title


class SignageCategory(models.Model):
    name = models.CharField(max_length=100, unique=True)
    image = models.ImageField(upload_to='category_images/')
    description = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.name


class SignageSubCategory(models.Model):
    name = models.CharField(max_length=100, unique=True)
    category = models.ForeignKey(SignageCategory, on_delete=models.CASCADE, related_name='subcategories')
    title = models.CharField(max_length=100)
    description = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.category.name} - {self.name}"



