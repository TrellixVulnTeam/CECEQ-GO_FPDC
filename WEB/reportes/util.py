from usuarios.models import UsuariosAnonimos
from reportes.models import *

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
