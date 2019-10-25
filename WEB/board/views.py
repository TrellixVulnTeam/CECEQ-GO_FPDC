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
        if get_error_user(request) == 1:
            error_message = "No tiene permiso para realizar esa acción"
            set_error_user(request, 0)
        users_anon = get_anonimus_users()
        args = {'users_anon': users_anon, 'error_message': error_message}
        return render(request, 'board/dashboard.html', args)
    return redirect('/')