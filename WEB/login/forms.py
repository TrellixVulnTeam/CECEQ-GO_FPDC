from django import forms

class LoginForm(forms.Form):
    nombre_usuario = forms.CharField(max_length=30, required=True)
    contrasena = forms.CharField(max_length=30, required=True)