# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect
from reportes.util import *
# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def dash(request):
    if request.user.is_authenticated:
        users_anon = get_anonimus_users()
        args= {'users_anon':users_anon}
        return render(request,'board/dashboard.html',args)
    return redirect('/')