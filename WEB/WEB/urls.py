
from django.contrib import admin

# Agregamos la libreria include para que funcione con las URLS de login
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('login.urls')),

]
