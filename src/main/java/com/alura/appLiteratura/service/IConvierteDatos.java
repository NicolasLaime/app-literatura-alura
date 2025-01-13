package com.alura.appLiteratura.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String Json, Class<T> clase);

}
