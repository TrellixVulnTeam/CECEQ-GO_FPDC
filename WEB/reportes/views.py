# Por defecto
from io import BytesIO
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import A4
from django.http import HttpResponse
from django.shortcuts import render, redirect
from reportes.util import *
import datetime
import time


# URLS de Reportes

def cursos(request):
    if request.user.is_authenticated:
        eventos = get_cursos()
        args = {'eventos': eventos}
        return render(request, 'reportes/cursos.html', args)
    return redirect('login')


def salones(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/salones.html')
    return redirect('login')


def usuarios(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/usuarios.html')
    return redirect('login')


def visitas(request):
    if request.user.is_authenticated:
        users_anon = get_anonimus_users_today()
        user_year = get_anonimus_users_year()
        user_month = get_anonimus_users_mes()
        u1 = get_anonimus_users_1()
        u2 = get_anonimus_users_2()
        u3 = get_anonimus_users_3()
        u4 = get_anonimus_users_4()
        u5 = get_anonimus_users_5()
        u6 = get_anonimus_users_6()
        u7 = get_anonimus_users_7()
        u8 = get_anonimus_users_8()
        u9 = get_anonimus_users_9()
        u10 = get_anonimus_users_10()
        u11 = get_anonimus_users_11()
        u12 = get_anonimus_users_12()

        args = {'user_anon': users_anon, 'user_year': user_year, 'u1': u1, 'u2': u2, 'u3': u3, 'u4': u4, 'u5': u5,
                'u6': u6, 'u7': u7, 'u8': u8, 'u9': u9, 'u10': u10, 'u11': u11, 'u12': u12, 'user_month': user_month,
                }
        return render(request, 'reportes/visitas.html', args)
    return redirect('login')


def show_users(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users()
        args = {'title': 'CECEQ Reportes', 'users_anon': users_anon, 'hoy': hoy}
        return render(request, 'reportes/usuarios.html', args)
    return redirect('login')


def reporte_usuarios_hoy(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%d/%m/%y")
        hora = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users_today()
        response = HttpResponse(content_type='application/pdf')
        response['Content-Disposition'] = 'attachment; filename=Hoy-reporte-usuarios.pdf'
        buffer = BytesIO()
        c = canvas.Canvas(buffer, pagesize=A4)

        c.setLineWidth(.3)
        c.drawString(30, 750, 'CECEQ -GO')
        c.drawString(30, 735, 'Reporte de Usuarios de hoy:')
        c.drawString(480, 750, hoy)
        c.drawString(480, 735, hora)
        c.line(460, 747, 560, 747)
        c.drawString(30, 700, 'El numero de usuarios hoy ha sido de:' + str(users_anon))

        c.save()
        pdf = buffer.getvalue()
        buffer.close()
        response.write(pdf)
        return response

    return redirect('login')


def reporte_usuarios_semana(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%d/%m/%y")
        hora = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users()
        response = HttpResponse(content_type='application/pdf')
        response['Content-Disposition'] = 'attachment; filename=Semana-reporte-usuarios.pdf'
        buffer = BytesIO()
        c = canvas.Canvas(buffer, pagesize=A4)

        c.setLineWidth(.3)
        c.drawString(30, 750, 'CECEQ -GO')
        c.drawString(30, 735, 'Reporte de Usuarios de esta semana:')
        c.drawString(480, 750, hoy)
        c.drawString(480, 735, hora)
        c.line(460, 747, 560, 747)
        c.drawString(30, 700, 'El numero de usuarios esta semana ha sido de:')
        c.save()
        pdf = buffer.getvalue()
        buffer.close()
        response.write(pdf)
        return response

    return redirect('login')


def reporte_usuarios_mes(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%d/%m/%y")
        hora = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users()
        response = HttpResponse(content_type='application/pdf')
        response['Content-Disposition'] = 'attachment; filename=Mes-reporte-usuarios.pdf'
        buffer = BytesIO()
        c = canvas.Canvas(buffer, pagesize=A4)

        c.setLineWidth(.3)
        c.drawString(30, 750, 'CECEQ -GO')
        c.drawString(30, 735, 'Reporte de Usuarios de este mes:')
        c.drawString(480, 750, hoy)
        c.drawString(480, 735, hora)
        c.line(460, 747, 560, 747)
        c.drawString(30, 700, 'El numero de usuarios este mes ha sido de:')
        c.save()
        pdf = buffer.getvalue()
        buffer.close()
        response.write(pdf)
        return response

    return redirect('login')


def reporte_usuarios_año(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%d/%m/%y")
        hora = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users_year()
        response = HttpResponse(content_type='application/pdf')
        response['Content-Disposition'] = 'attachment; filename=Año-reporte-usuarios.pdf'
        buffer = BytesIO()
        c = canvas.Canvas(buffer, pagesize=A4)

        c.setLineWidth(.3)
        c.drawString(30, 750, 'CECEQ -GO')
        c.drawString(30, 735, 'Reporte de Usuarios de este año:')
        c.drawString(480, 750, hoy)
        c.drawString(480, 735, hora)
        c.line(460, 747, 560, 747)
        c.drawString(30, 700, 'El numero de usuarios este año ha sido de:' + str(users_anon))
        c.save()
        pdf = buffer.getvalue()
        buffer.close()
        response.write(pdf)
        return response

    return redirect('login')
