#Variable de sesion para errores de login
def set_error_login(request, error_message):
    request.session['error_login'] = error_message

def get_error_login(request):
    if 'error_login' in request.session:
        return request.session['error_login']
    return None