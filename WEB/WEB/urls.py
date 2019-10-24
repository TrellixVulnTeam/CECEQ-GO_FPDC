
from django.contrib import admin

# Agregamos la libreria include para que funcione con las URLS de login
from django.contrib.auth.views import login_required,logout_then_login
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),

    path('', include('login.urls')),
    path('usuarios/', include('usuarios.urls')),
    path('dashboard/', include('board.urls')),
    path('mapas/', include('mapas.urls')),
    path('reportes/', include('reportes.urls')),
    path('instalaciones/', include('instalaciones.urls')),
]
