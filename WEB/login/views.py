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
from login.util import *


def login(request):
    error_message = ""
    if not request.user.is_authenticated:
        if request.method == "POST":
            # Añadimos los datos recibidos al formulario
            form = LoginForm(request.POST)
            # Si el formulario es válido...
            if form.is_valid():
                # Recuperamos las credenciales validadas
                usuario = form.cleaned_data['nombre_usuario']
                contrasena = form.cleaned_data['contrasena']

                # Verificamos las credenciales del usuario
                user = authenticate(request=request, username=usuario, password=contrasena)
                # Si existe un usuario con ese nombre y contraseña
                error = get_error_login(request)
                if error == 1:
                    error_message = "Usuario y/o contraseña incorrectos"
                elif error == 2:
                    error_message = "No tiene permiso para entrar a este portal"
                elif error == 3:
                    error_message = "Su usuario se encuentra desactivado en el momento"
                elif user is not None:
                    # Hacemos el login manualmente
                    do_login(request, user)
                    # Y le redireccionamos a la portada
                    return redirect('board')
    else:
        return redirect('board')
    print("Redirect del login")
    args = {'title': 'Inicio de Sesion', 'error_message':error_message}
    return render(request, "login/login.html", args)


def logout(request):
    # Finalizamos la sesión
    do_logout(request)
    # Redireccionamos a la portada
    return redirect('/')
