# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect
from django.http import HttpResponse
from reportes.util import *
from django.contrib.staticfiles.templatetags.staticfiles import *
from django.template.defaulttags import register
from django.core.files.storage import default_storage
from django.contrib.staticfiles import finders
from django.conf import settings

def mapasA(request):
    if request.user.is_authenticated:
        cursos = get_espacios()
        args = {'cursos': cursos}
        return render(request, 'mapas/mapasA.html',args)
    return redirect('login')
def mapasB(request):
    if request.user.is_authenticated:
        return render(request, 'mapas/mapasB.html')
    return redirect('login')


@register.filter(name='image_exists')
def image_exists(image):
    return default_storage.exists(settings.STATIC_ROOT+image)
