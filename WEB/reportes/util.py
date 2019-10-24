from usuarios.models import *
from reportes.models import *
import time
from datetime import date
def get_anonimus_users():
    users_r = UsuariosAnonimos.objects.all()
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_espacios():
    espacios = Espacios.objects.all()
    list_of_ids = []
    for espacio in espacios:
        list_of_ids.append(espacio.id_espacio)
    return Espacios.objects.filter(id_espacio__in=list_of_ids)

def get_cursos():
    eventos = Eventos.objects.all()
    list_of_ids = []
    for evento in eventos:
        list_of_ids.append(evento.id_evento)
    return Eventos.objects.filter(id_evento__in=list_of_ids)


def get_anonimus_users_year():
    today  = date.today()
    users_r = UsuariosAnonimos.objects.filter(time__year =today.year)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_today():
    today  = date.today()
    users_r = UsuariosAnonimos.objects.filter(time__year =today.year,time__month=today.month,time__day=today.day)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_month():
    today  = date.today()
    users_r = UsuariosAnonimos.objects.filter(time__month=today.month)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number


