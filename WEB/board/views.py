# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect
from reportes.util import *
# LLamamos a los metodos por HHTPS
from django.http import HttpResponse
from board.util import *

def dash(request):
    error_message = ""
    if request.user.is_authenticated:
        users_anon = get_anonimus_users_today()
        users_month = get_anonimus_users_mes()
        users_year = get_anonimus_users_year()
        if get_error_user(request) == 1:
            error_message = "No tiene permiso para realizar esa acción"
            set_error_user(request, 0)
        users_anon = get_anonimus_users()
        args = {'users_anon':users_anon,'users_month':users_month,'users_year':users_year, 'error_message': error_message}
        return render(request, 'board/dashboard.html', args)
    return redirect('/')