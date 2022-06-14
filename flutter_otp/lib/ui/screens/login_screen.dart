import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:cool_alert/cool_alert.dart';
import 'package:flutter_otp/core/constants/color.dart';
import 'package:flutter_otp/core/helpers.dart';
import 'package:flutter_otp/data/domain/api_response/success_response.dart';
import 'package:flutter_otp/data/domain/model/login.dart';
import 'package:flutter_otp/data/domain/model/loginDTO.dart';
import 'package:flutter_otp/logic/cubit/auth/auth_cubit.dart';
import 'package:flutter_otp/logic/cubit/auth/auth_state.dart';
import 'package:flutter_otp/ui/screens/otp_screen.dart';
import 'package:flutter_otp/ui/widgets/custom_button.dart';
import 'package:flutter_otp/ui/widgets/custom_form_field.dart';
import 'package:flutter_otp/ui/widgets/custom_rich_text.dart';
import 'package:flutter_otp/ui/widgets/dialog_builder.dart';

class LoginScreen extends StatefulWidget {

  static const String routeName = "/login";

  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  bool _hidePassword = true;

  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  String get email => _emailController.text.trim();
  String get password => _passwordController.text.trim();
  final GlobalKey<CustomFormFieldState> usernameKey = GlobalKey();
  final GlobalKey<CustomFormFieldState> passwordKey = GlobalKey();

  void _togglePasswordVisibility() {
    setState(() {
      _hidePassword = !_hidePassword;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: BlocListener<AuthCubit, AuthState>(
          listener: (_, state) {
            if (state is AuthLoading) {
              DialogBuilder(context).showLoadingIndicator("Processing ...");
            } else if (state is AuthError) {
              DialogBuilder(context).hideOpenDialog();
              _error(state.message);
            } else if (state is AuthNetworkError) {
              DialogBuilder(context).hideOpenDialog();
              _error(state.message);
            }else if (state is AuthSuccess) {
              DialogBuilder(context).hideOpenDialog();
              final response = state.response as SuccessResponse;
              final LoginDTO loginDTO = LoginDTO(jwtValue: response.data, username: email);

              Navigator.pushReplacementNamed(context, OtpScreen.routeName, arguments: loginDTO);
            }
          },
          child: SafeArea(
              child: Stack(
                children: [
                  Container(
                    height: MediaQuery.of(context).size.height,
                    width: MediaQuery.of(context).size.width,
                    color: Theme.of(context).primaryColor,
                  ),
                  Positioned(
                    top: MediaQuery.of(context).size.height * 0.08,
                    child: Container(
                      height: MediaQuery.of(context).size.height * 0.9,
                      width: MediaQuery.of(context).size.width,
                      decoration: const BoxDecoration(
                          color: whiteShade,
                          borderRadius: BorderRadius.only(
                              topLeft: Radius.circular(40),
                              topRight: Radius.circular(40))),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Container(
                            height: 100,
                            width: MediaQuery.of(context).size.width * 0.8,
                            margin: EdgeInsets.only(
                                left: MediaQuery.of(context).size.width * 0.09, top: MediaQuery.of(context).size.width * 0.09),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Text("Sign In",
                                    style: TextStyle(
                                        color: Theme.of(context).primaryColor, fontSize: 30)),
                                const SizedBox(
                                  height: 24,
                                ),
                                Text("Welcome Back",
                                    style: TextStyle(
                                        color: Theme.of(context).primaryColor, fontSize: 14))
                              ],
                            ),
                          ),
                          const SizedBox(
                            height: 24,
                          ),
                          CustomFormField(
                              key: usernameKey,
                              headingText: "Email",
                              hintText: "Email",
                              obscureText: false,
                              suffixIcon: const SizedBox(),
                              controller: _emailController,
                              maxLines: 1,
                              textInputAction: TextInputAction.done,
                              textInputType: TextInputType.emailAddress,
                              validator: (val) {
                                if (val!.isEmpty) {
                                  return "Please enter the Email";
                                } else if (!Helpers.checkValidEmail(val)) {
                                  return "Please enter a valid email address";
                                } else {
                                  return null;
                                }
                              }
                          ),
                          const SizedBox(
                            height: 16,
                          ),
                          CustomFormField(
                            key: passwordKey,
                            headingText: "Password",
                            maxLines: 1,
                            textInputAction: TextInputAction.done,
                            textInputType: TextInputType.text,
                            hintText: "At least 8 Character",
                            obscureText: _hidePassword,
                            suffixIcon: InkWell(
                              onTap: () {
                                _togglePasswordVisibility();
                              },
                              child: Icon(
                                _hidePassword ? Icons.visibility_off : Icons.visibility,
                                color: _hidePassword
                                    ? Colors.grey
                                    : primary,
                              ),
                            ),
                            controller: _passwordController,
                            validator: (val) {
                              return Helpers.passwordValidation(val!);
                            },
                          ),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              Container(
                                margin: const EdgeInsets.symmetric(
                                    vertical: 16, horizontal: 24),
                                child: InkWell(
                                  onTap: () {},
                                  child: Text(
                                    "Forgot Password?",
                                    style: TextStyle(
                                        color: blue.withOpacity(0.7),
                                        fontWeight: FontWeight.w500),
                                  ),
                                ),
                              ),
                            ],
                          ),
                          AuthButton(
                            onTap: () => _loginFormSubmit(),
                            text: 'Sign In',
                          ),
                          CustomRichText(
                            description: "Don't already Have an account? ",
                            text: "Sign Up",
                            onTap: () {},
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              )),
        )
    );
  }

  void _error(String message) {
    CoolAlert.show(
      context: context,
      type: CoolAlertType.error,
      title: 'Oops...',
      text: message,
      loopAnimation: false,
    );
  }

  void _loginFormSubmit() {
    if (usernameKey.currentState!.validate() != null || passwordKey.currentState!.validate() != null) {
      return;
    }
    // call api for login
   BlocProvider.of<AuthCubit>(context).userLogin(Login(username: email, password: password));
  }
}

