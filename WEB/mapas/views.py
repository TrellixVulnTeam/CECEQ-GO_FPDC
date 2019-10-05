# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect
from django.http import HttpResponse

def mapasA(request):
    if request.user.is_authenticated:
        return render(request, 'mapas/mapasA.html')
    return redirect('login')
def mapasB(request):
    if request.user.is_authenticated:
        return render(request, 'mapas/mapasB.html')
    return redirect('login')