# Llamamos al modelo
from urllib import request

from background_task import background

from django.utils import timezone
from django.utils.timezone import make_naive

from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
from usuarios.models import Roles
from background_task.models import Task
# Llamamos a los tags para poder registrar los filtros
from django.template.defaulttags import register

def get_non_registered_users():
    users_r = UsuariosRegistrados.objects.all()
    list_of_ids = []
    for user_r in users_r:
        list_of_ids.append(user_r.id)
    return Usuario.objects.exclude(id_usuario__in=list_of_ids)

def get_username(id):
    users_nr = Usuario.objects.filter(id_usuario=id)
    return users_nr.first().nombre_usuario

def get_registered_users():
    users_r = UsuariosRegistrados.objects.all()
    list_of_ids = []
    for user_r in users_r:
        list_of_ids.append(user_r.id)
    return Usuario.objects.filter(id_usuario__in=list_of_ids)

@register.filter(name='get_status')
def get_status_single_user(id):
    users_r = UsuariosRegistrados.objects.filter(id=id)
    return users_r.first().is_active

@register.filter(name='get_fecha')
def get_date_single_user(id):
    users_r = UsuariosRegistrados.objects.filter(id=id)
    return users_r.first().date_joined

def get_status_registered_user():
    users_r = get_registered_users()
    dictionary = {}
    for user_r in users_r:
        dictionary[user_r.id_usuario] = get_status_single_user(user_r.id_usuario)
    return dictionary

#Funcion para modificar variable de sesion
@register.filter(name='set_add_user')
def set_add_user(request, id_usuario):
    request.session['add_user'] = id_usuario

#Funcion para obtener variable de sesion
def get_add_user():
    if 'add_user' in request.session:
        return request.session['add_user']
    return None
def add_user_in_database(id_new, username):
    registro = UsuariosRegistrados(id=id_new, username=username)
    registro.save()

def delete_user_in_database(id):
    instance = UsuariosRegistrados.objects.get(id=id)
    instance.delete()

def perma_activate_user_in_database(id):
    if Task.objects.filter(verbose_name=id).exists():
        instance = Task.objects.get(verbose_name=id)
        instance.delete()
    instance = UsuariosRegistrados.objects.get(id=id)
    instance.is_active = 1
    instance.save()

@background(schedule=60)
def temp_activate_user_in_database(id):
    instance = UsuariosRegistrados.objects.get(id=id)
    instance.is_active = 1
    instance.save()

def perma_deactivate_user_in_database(id, opcion):
    instance = UsuariosRegistrados.objects.get(id=id)
    instance.is_active = 0
    instance.save()
    if opcion == "2":
        t1 = timezone.localtime() + timezone.timedelta(minutes=3)
        #t1 -= make_naive(t1, timezone=timezone.utc)-make_naive(t1)
        temp_activate_user_in_database(id, schedule=t1, verbose_name=id)
    elif opcion == "3":
        t1 = timezone.localtime() + timezone.timedelta(minutes=7)
        #t1 -= make_naive(t1, timezone=timezone.utc) - make_naive(t1)
        temp_activate_user_in_database(id, schedule=t1, verbose_name=id)
    elif opcion == "4":
        t1 = timezone.localtime() + timezone.timedelta(minutes=15)
        #t1 -= make_naive(t1, timezone=timezone.utc) - make_naive(t1)
        temp_activate_user_in_database(id, schedule=t1, verbose_name=id)
    elif opcion == "5":
        t1 = timezone.localtime() + timezone.timedelta(minutes=30)
        #t1 -= make_naive(t1, timezone=timezone.utc) - make_naive(t1)
        temp_activate_user_in_database(id, schedule=t1, verbose_name=id)

def modificar_rol_usuario(id, rol):
    if id == "1":
        id_r = 1
    elif id == "2":
        id_r = 2
    elif id == "3":
        id_r = 3
    instance = UsuariosRegistrados.objects.get(id=id_r)
    instance_rol = Roles.objects.get(id=rol)
    instance.rol = instance_rol
    instance.save()


#Filters created in order to properly call functions from the template

#Filter in case of dictionary implementation, more information on:
#https://stackoverflow.com/questions/8000022/django-template-how-to-look-up-a-dictionary-value-with-a-variable
@register.filter(name='get_item')
def get_item(dictionary, key):
    return dictionary.get(key)