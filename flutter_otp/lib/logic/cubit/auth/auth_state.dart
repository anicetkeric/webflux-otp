import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

@immutable
abstract class AuthState extends Equatable {
  @override
  List<Object> get props => [];
}

class AuthInitial extends AuthState {
}

class AuthEmpty extends AuthState {
}

class AuthLoading extends AuthState {
}

class AuthNetworkError extends AuthState {
  final String message;

  AuthNetworkError({
    required this.message,
  });

  @override
  List<Object> get props => [message];
}

class AuthError extends AuthState {
  final String message;

  AuthError({
    required this.message,
  });

  @override
  List<Object> get props => [message];
}

class AuthLoaded extends AuthState {
  final dynamic auth;

  AuthLoaded({
    required this.auth,
  });

  @override
  List<Object> get props => [auth];
}

