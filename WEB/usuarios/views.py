# Por defecto
from django.shortcuts import render
# Llamamos a los metodos por HHTPS
from django.http import HttpResponse
# Llamamos al modelo
from django.contrib.auth import logout as do_logout
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
from django.shortcuts import render, redirect

def perfil(request):
    if request.user.is_authenticated:
        return render(request, 'usuarios/perfil.html')
    return redirect('login')
def get_non_registered_users():
    users_r = UsuariosRegistrados.objects.all()
    list_of_ids = []
    for user_r in users_r:
        list_of_ids.append(user_r.user_id)
    return Usuario.objects.exclude(id_usuario__in=list_of_ids)

def get_registered_users():
    users_r = UsuariosRegistrados.objects.all()
    list_of_ids = []
    for user_r in users_r:
        list_of_ids.append(user_r.user_id)
    return Usuario.objects.filter(id_usuario__in=list_of_ids)


def show_users(request):
    if request.user.is_authenticated:
        users = get_registered_users()
        users_noR= get_non_registered_users()
        args = {'title':'CECEQ USUARIOS', 'users':users,'users_noR':users_noR}
        return render(request,'usuarios/usuarios.html', args)
    return redirect('login')
def show_modal_user(request):
    return 0;