# Por defecto
from django.shortcuts import render
# Llamamos a los metodos por HHTPS
from django.http import HttpResponse
# Llamamos al modelo
from django.contrib.auth import logout as do_logout
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
from django.shortcuts import render, redirect
from django.template.defaulttags import register

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

def get_status_single_user(id):
    users_r = UsuariosRegistrados.objects.filter(user_id=id)
    return users_r.first().estado

def get_status_registered_user():
    users_r = get_registered_users()
    dictionary = {}
    for user_r in users_r:
        dictionary[user_r.id_usuario] = get_status_single_user(user_r.id_usuario)
    return dictionary

def show_users(request):
    if request.user.is_authenticated:
        users = get_registered_users()
        users_noR= get_non_registered_users()
        estado = get_status_registered_user()
        args = {'title':'CECEQ USUARIOS', 'users':users,'users_noR':users_noR, 'estado':estado}
        return render(request,'usuarios/usuarios.html', args)
    return redirect('login')
def show_modal_user(request):
    return 0;

@register.filter
def get_item(dictionary, key):
    return dictionary.get(key)