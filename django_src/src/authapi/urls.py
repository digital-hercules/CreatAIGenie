from django.urls import path, include
from rest_framework import routers
#from .views import ( UserViewSet)

router = routers.DefaultRouter()

urlpatterns = [
    path('api/', include(router.urls)),
]
