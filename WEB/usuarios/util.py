# Llamamos al modelo
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
# Llamamos a los tags para poder registrar los filtros
from django.template.defaulttags import register

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

@register.filter(name='get_status')
def get_status_single_user(id):
    users_r = UsuariosRegistrados.objects.filter(user_id=id)
    return users_r.first().estado

@register.filter(name='get_fecha')
def get_date_single_user(id):
    users_r = UsuariosRegistrados.objects.filter(user_id=id)
    return users_r.first().fecha_inicio

def get_status_registered_user():
    users_r = get_registered_users()
    dictionary = {}
    for user_r in users_r:
        dictionary[user_r.id_usuario] = get_status_single_user(user_r.id_usuario)
    return dictionary
#Filters created in order to properly call functions from the template

#Filter in case of dictionary implementation, more information on:
#https://stackoverflow.com/questions/8000022/django-template-how-to-look-up-a-dictionary-value-with-a-variable
@register.filter(name='get_item')
def get_item(dictionary, key):
    return dictionary.get(key)