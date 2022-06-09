import 'package:flutter/material.dart';
import 'package:flutter_otp/core/constants/color.dart';
import 'package:flutter_otp/screens/otp_screen.dart';
import 'package:flutter_otp/widgets/custom_button.dart';
import 'package:flutter_otp/widgets/custom_form_field.dart';
import 'package:flutter_otp/widgets/custom_rich_text.dart';

class LoginScreen extends StatefulWidget {

  static const String routeName = "/login";

  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  String get email => _emailController.text.trim();
  String get password => _passwordController.text.trim();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
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
                        headingText: "Email or Username",
                        hintText: "Email or Username",
                        obscureText: false,
                        suffixIcon: const SizedBox(),
                        controller: _emailController,
                        maxLines: 1,
                        textInputAction: TextInputAction.done,
                        textInputType: TextInputType.emailAddress,
                      ),
                      const SizedBox(
                        height: 16,
                      ),
                      CustomFormField(
                        headingText: "Password",
                        maxLines: 1,
                        textInputAction: TextInputAction.done,
                        textInputType: TextInputType.text,
                        hintText: "At least 8 Character",
                        obscureText: true,
                        suffixIcon: IconButton(
                            icon: const Icon(Icons.visibility), onPressed: () {}),
                        controller: _passwordController,
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
                        onTap: () {Navigator.pushReplacement(
                            context,
                            MaterialPageRoute(
                                builder: (context) => const OtpScreen(userEmail: "anicetkouame@yahoo.fr",)));},
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
    );
  }
}

