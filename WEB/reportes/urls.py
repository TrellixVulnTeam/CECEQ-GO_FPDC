# Importar la libreria para URLS
from django.urls import path

# Uso del "punto" para referenciar esta libreria e importar el views
from . import views

urlpatterns = [

    # primero se pondra el nombre de la ruta a llama
    # Se pone el nombre views seguido de un nombre que se le dara
    # Despues se pondra el nombre en este caso login
    path('reporte-cursos/', views.cursos, name='re-cursos'),
    path('reporte-salones/', views.salones, name='re-salones'),
    path('reporte-usuarios/', views.show_users, name='re-usuarios'),
    path('reporte-vistas/', views.visitas, name='re-vistas'),
    path('reporte-hoy/', views.reporte_usuarios_hoy, name='reporte-hoy'),
    path('reporte-semana/', views.reporte_usuarios_semana, name='reporte-semana'),
    path('reporte-año/', views.reporte_usuarios_año, name='reporte-anio'),
    path('reporte-mes/', views.reporte_usuarios_mes, name='reporte-mes'),







]