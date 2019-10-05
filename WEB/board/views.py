# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def dash(request):
    if request.user.is_authenticated:
        return render(request,'board/dashboard.html')
    return redirect('login')