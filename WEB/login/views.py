# Por defecto
from django.shortcuts import render
from django.contrib.auth import logout as do_logout
from django.contrib.auth import authenticate
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth import login as do_login
from django.shortcuts import redirect
# LLamamos a los metodos por HHTPS
from django.http import HttpResponse
from login.forms import LoginForm
# Llamamos a la señal
from django.core.signals import *


def login(request):
    if not request.user.is_authenticated:
        print("el usuario esta autenticado")
        if request.method == "POST":
            print("El método es POST")
            # Añadimos los datos recibidos al formulario
            form = LoginForm(request.POST)
            # Si el formulario es válido...
            if form.is_valid():
                # Recuperamos las credenciales validadas
                usuario = form.cleaned_data['nombre_usuario']
                contrasena = form.cleaned_data['contrasena']

                # Verificamos las credenciales del usuario
                print(usuario)
                print(contrasena)
                user = authenticate(username=usuario, password=contrasena)
                # Si existe un usuario con ese nombre y contraseña
                if user is not None:
                    # Hacemos el login manualmente
                    print("doing login...")
                    do_login(request, user)
                    print("login completed")
                    # Y le redireccionamos a la portada
                    return redirect('board')
    else:
        return redirect('board')
    print("Redirect del login")
    return render(request, "login/login.html", {'title': 'Inicio de Sesion'})


def logout(request):
    # Finalizamos la sesión
    do_logout(request)
    # Redireccionamos a la portada
    return redirect('/')
