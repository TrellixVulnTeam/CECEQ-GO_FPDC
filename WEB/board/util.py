


#Funcion para modificar variable de sesion
def set_error_user(request, error):
    request.session['error_permiso'] = error

#Funcion para obtener variable de sesion
def get_error_user():
    if 'error_permiso' in request.session:
        return request.session['error_permiso']
    return None