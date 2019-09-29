# Por defecto
from django.shortcuts import render

# Llamamos a los metodos por HHTPS
from django.http import HttpResponse

# Llamamos al modelo
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados

def show_users(request):
    users = Usuario.objects.filter(id_usuario=1)
    print(users)
    return render(request,'usuarios/usuarios.html',{'title': 'CECEQ USUARIOS'})
