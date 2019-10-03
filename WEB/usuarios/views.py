# Por defecto
from django.shortcuts import render

# Llamamos a los metodos por HHTPS
from django.http import HttpResponse

# Llamamos al modelo
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados

def show_users(request):
    users = Usuario.objects.all()
    args = {'title':'CECEQ USUARIOS', 'users':users}
    return render(request,'usuarios/usuarios.html', args)
