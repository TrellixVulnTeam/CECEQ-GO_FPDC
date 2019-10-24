from usuarios.models import *
from reportes.models import *
import time
from datetime import *
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
    users_r = UsuariosAnonimos.objects.filter(time__year = today.year)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_today():
    today  = date.today()
    users_r = UsuariosAnonimos.objects.filter(time = today)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_mes():
    year = datetime.now().year
    mes = datetime.now().month
    users_r = UsuariosAnonimos.objects.filter(time__year = year,time__month=mes)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

#----------------------------filtro cada mes---------------
def get_anonimus_user_general(month):
    users_r = UsuariosAnonimos.objects.filter(time__month= month)
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_1():
    users_r = UsuariosAnonimos.objects.filter(time__month = '1')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_2():
    users_r = UsuariosAnonimos.objects.filter(time__month = '2')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_3():
    users_r = UsuariosAnonimos.objects.filter(time__month = '3')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_4():
    users_r = UsuariosAnonimos.objects.filter(time__month = '4')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_5():
    users_r = UsuariosAnonimos.objects.filter(time__month = '5')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_6():
    users_r = UsuariosAnonimos.objects.filter(time__month = '6')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_7():
    users_r = UsuariosAnonimos.objects.filter(time__month = '7')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_8():
    users_r = UsuariosAnonimos.objects.filter(time__month = '8')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_9():
    users_r = UsuariosAnonimos.objects.filter(time__month = '9')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_10():
    users_r = UsuariosAnonimos.objects.filter(time__month = '10')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_11():
    users_r = UsuariosAnonimos.objects.filter(time__month = '11')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

def get_anonimus_users_12():
    users_r = UsuariosAnonimos.objects.filter(time__month = '12')
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number

