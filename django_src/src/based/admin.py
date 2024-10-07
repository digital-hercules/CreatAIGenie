from django.contrib import admin
from .models import (
    UserProfile, Document, ProcessedData, SubscriptionPlan, UserSubscription,
    SignageCategory, SignageSubCategory,Signagetemp
)

@admin.register(UserProfile)
class UserProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'location', 'birth_date')
    search_fields = ('user__username', 'location')

@admin.register(Document)
class DocumentAdmin(admin.ModelAdmin):
    list_display = ('title', 'user_profile', 'uploaded_at')
    list_filter = ('uploaded_at',)
    search_fields = ('title', 'description')

@admin.register(ProcessedData)
class ProcessedDataAdmin(admin.ModelAdmin):
    list_display = ('document', 'processed_at')
    list_filter = ('processed_at',)

@admin.register(SubscriptionPlan)
class SubscriptionPlanAdmin(admin.ModelAdmin):
    list_display = ('name', 'price', 'duration_days')

@admin.register(UserSubscription)
class UserSubscriptionAdmin(admin.ModelAdmin):
    list_display = ('user_profile', 'plan', 'start_date', 'end_date', 'is_active')
    list_filter = ('plan', 'start_date', 'end_date')
    search_fields = ('user_profile__user__username',)

@admin.register(SignageCategory)
class SignageCategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'description', 'created_at')

@admin.register(SignageSubCategory)
class SignageSubCategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'category', 'title', 'created_at')

@admin.register(Signagetemp)
class SignagetempAdmin(admin.ModelAdmin):
    list_display = ('title', 'category', 'width', 'height', 'created_at', 'updated_at')
    list_filter = ('category', 'created_at')
    search_fields = ('title', 'description')
    readonly_fields = ('created_at', 'updated_at')
