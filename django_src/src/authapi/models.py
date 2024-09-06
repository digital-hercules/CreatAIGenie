#from google.cloud import firestore
from django.conf import settings
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
#from django.utils import timezone
from django.db import models
#import logging

class UserManager(BaseUserManager):
    def create_user(self, email, name, password=None):
        if not email:
            raise ValueError('Users must have an email address')
        user = self.model(email=self.normalize_email(email), name=name)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, name, password=None):
        user = self.create_user(email, name, password)
        user.is_admin = True
        user.is_staff = True
        user.save(using=self._db)
        return user

class User(AbstractBaseUser):
    email = models.EmailField(verbose_name="Email", max_length=255, unique=True)
    name = models.CharField(max_length=255)
    is_active = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['name']

    def __str__(self):
        return self.name

    def has_perm(self, perm, obj=None):
        return self.is_admin

    def has_module_perms(self, app_label):
        return self.is_admin


class Profile(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    bio = models.TextField()

    def __str__(self):
        return self.user.name
    
'''
db = firestore.Client()

# Setup logger
logger = logging.getLogger(__name__)

class UserManager(BaseUserManager):
    def create_user(self, email, name, password=None):
        if not email:
            raise ValueError('Users must have an email address')
        user = self.model(email=self.normalize_email(email), name=name)
        user.set_password(password)
        user.save()
        return user

    def create_superuser(self, email, name, password=None):
        user = self.create_user(email, name, password)
        user.is_admin = True
        user.is_staff = True
        user.save()
        return user

class User(AbstractBaseUser):
    email = models.EmailField(unique=True)
    name = models.CharField(max_length=255)
    is_active = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['name']

    def __str__(self):
        return self.name

    def has_perm(self, perm, obj=None):
        return self.is_admin

    def has_module_perms(self, app_label):
        return self.is_admin

    def save(self, *args, **kwargs):
        self.updated_at = timezone.now()
        user_ref = db.collection('users').document(self.email)
        try:
            user_ref.set(self.to_dict())
        except Exception as e:
            logger.error(f"Failed to save user {self.email} to Firestore: {str(e)}")
            raise

    def to_dict(self):
        return {
            'email': self.email,
            'name': self.name,
            'is_active': self.is_active,
            'is_admin': self.is_admin,
            'is_staff': self.is_staff,
            'created_at': self.created_at,
            'updated_at': self.updated_at,
        }

    @classmethod
    def get_by_email(cls, email):
        user_ref = db.collection('users').document(email)
        try:
            user_data = user_ref.get().to_dict()
            if user_data:
                return cls(**user_data)
        except Exception as e:
            logger.error(f"Failed to retrieve user {email} from Firestore: {str(e)}")
        return None

class Profile:
    def __init__(self, user, bio):
        self.user = user
        self.bio = bio

    def __str__(self):
        return self.user.name

    def save(self):
        profile_ref = db.collection('profiles').document(self.user.email)
        try:
            profile_ref.set({
                'user_email': self.user.email,
                'bio': self.bio,
            })
        except Exception as e:
            logger.error(f"Failed to save profile for user {self.user.email} to Firestore: {str(e)}")
            raise

    @classmethod
    def get_by_user(cls, user):
        profile_ref = db.collection('profiles').document(user.email)
        try:
            profile_data = profile_ref.get().to_dict()
            if profile_data:
                return cls(user, profile_data['bio'])
        except Exception as e:
            logger.error(f"Failed to retrieve profile for user {user.email} from Firestore: {str(e)}")
        return None
'''