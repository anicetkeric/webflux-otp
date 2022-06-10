import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_otp/core/constants/color.dart';
import 'package:flutter_otp/core/helpers.dart';
import 'package:pin_code_fields/pin_code_fields.dart';


class OtpScreen extends StatefulWidget {

  static const String routeName = "/otp";

  final String userEmail;

  const OtpScreen({Key? key, required this.userEmail}) : super(key: key);

  @override
  State<OtpScreen> createState() => _OtpScreenState();
}

class _OtpScreenState extends State<OtpScreen> with TickerProviderStateMixin {

  TextEditingController textEditingController = TextEditingController();
  late StreamController<ErrorAnimationType> errorController;

  bool hasError = false;
  bool timeExpiration = false;
  String currentText = "";
  final GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();
  final formKey = GlobalKey<FormState>();

  late AnimationController _controller;
  int levelClock = 600; // int in seconds

  @override
  void initState() {
    errorController = StreamController<ErrorAnimationType>();

    _controller = AnimationController( vsync: this ,duration: Duration( seconds:levelClock));

    //  Start the animation
    _controller.forward();
    super.initState();
  }

  @override
  void dispose() {
    errorController.close();
    _controller.dispose();
    super.dispose();
  }

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
                  child: SizedBox(
                    height: MediaQuery.of(context).size.height,
                    width: MediaQuery.of(context).size.width,
                    child: ListView(
                      children: <Widget>[
                        const SizedBox(height: 30),
                        _buildTextAndImage(),
                        const SizedBox(height: 8),
                        _buildUserEmailText(),
                        const SizedBox(
                          height: 20,
                        ),
                        Form(
                          key: formKey,
                          child: Padding(
                              padding: const EdgeInsets.symmetric(
                                  vertical: 8.0, horizontal: 30),
                              child: PinCodeTextField(
                                appContext: context,
                                pastedTextStyle: TextStyle(
                                  color: Colors.green.shade600,
                                  fontWeight: FontWeight.bold,
                                ),
                                length: 4,
                                obscureText: false,
                                obscuringCharacter: '*',
                                animationType: AnimationType.fade,
                                pinTheme: PinTheme(
                                  shape: PinCodeFieldShape.box,
                                  borderRadius: BorderRadius.circular(0),
                                  fieldHeight: 40,
                                  fieldWidth: 60,
                                  inactiveFillColor: Colors.grey.shade200,
                                  activeFillColor: hasError ? Colors.orange : Colors.white,
                                ),
                                validator: (v) {
                                  print("Pin form size ${v!.length}");
                                  return "";
                                },
                                cursorColor: Colors.black,
                                animationDuration: const Duration(milliseconds: 300),
                                textStyle: const TextStyle(fontSize: 20, height: 1.6),
                                // backgroundColor: Colors.blue.shade50,
                                enableActiveFill: true,
                                errorAnimationController: errorController,
                                controller: textEditingController,
                                keyboardType: TextInputType.number,
                                boxShadows: const [
                                  BoxShadow(
                                    offset: Offset(0, 1),
                                    color: Colors.black12,
                                    blurRadius: 10,
                                  )
                                ],
                                onCompleted: (v) {
                                  formKey.currentState!.validate();

                                  if(currentText.length == 4 && !_controller.isCompleted){
                                    setState(() {
                                      hasError = false;
                                    });
                                  }
                                  if(_controller.isCompleted){
                                    timeExpiration = true;
                                    errorController.add(ErrorAnimationType.shake);
                                  }

                                },
                                // onTap: () {
                                //   print("Pressed");
                                // },
                                onChanged: (value) {
                                  print(value);
                                  setState(() {
                                    currentText = value;
                                  });
                                },
                                beforeTextPaste: (text) {
                                  print("Allowing to paste $text");
                                  //if you return true then it will show the paste confirmation dialog. Otherwise if false, then nothing will happen.
                                  //but you can show anything you want here, like your pop up saying wrong paste format or etc
                                  return true;
                                },
                              )),
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 30.0),
                          child: Text(
                            hasError ? "Pin code not correct, please try again" :
                            timeExpiration ? "Code time expired, please resend the code." : "",
                            style: const TextStyle(
                                color: Colors.red,
                                fontSize: 12,
                                fontWeight: FontWeight.w400),
                          ),
                        ),
                        const SizedBox(
                          height: 20,
                        ),
                        _buildTimer(),
                        const SizedBox(
                          height: 10,
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        _resendCode(),
                        const SizedBox(
                          height: 40,
                        ),
                        Center(child:
                        InkWell(
                          onTap: () {},
                          child: Text(
                            "Back to Login",
                            style: TextStyle(
                                color: blue.withOpacity(0.7),
                                fontWeight: FontWeight.w500),
                          ),
                        ),
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ],
          )),
    );
  }

  Row _buildTimer() {
    final Animation<int> animation = StepTween(begin: levelClock, end: 0,).animate(_controller);
    Duration clockTimer = Duration(seconds: animation.value);

    String timerText =
        '${clockTimer.inMinutes.remainder(60).toString()}:${clockTimer.inSeconds.remainder(60).toString().padLeft(2, '0')}';

    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Text("This code will expired in ", style: TextStyle(color: Colors.black54, fontSize: 15)),
        Text(
          timerText,
          style: const TextStyle(color: optTextColor, fontSize: 15,fontWeight: FontWeight.bold),
        )
      ],
    );
  }

  Widget _buildTextAndImage() {
    return Column(children: <Widget>[
      RichText(
        textAlign: TextAlign.center,
        text: TextSpan(
          text:  "Check your email",
          style: TextStyle(
              color: Theme.of(context).primaryColor,
              fontSize: 30,
              fontWeight: FontWeight.bold),
        ),
      ),
      Container(
          margin: const EdgeInsets.symmetric(vertical: 10),
          child: RichText(
            textAlign: TextAlign.center,
            text: TextSpan(
              text: "We have sent the code to your email",
              style: TextStyle(
                  color: Theme.of(context).accentColor,
                  fontSize: 18),
            ),
          )),
      Image.asset("assets/images/email.png", width: 150, height: 150),
    ]);
  }

  Widget _buildUserEmailText(){
    return Padding(
      padding:
      const EdgeInsets.symmetric(horizontal: 30.0, vertical: 8),
      child: RichText(
        text: TextSpan(
            text: "Enter the code sent to ",
            children: [
              TextSpan(
                  text: Helpers.maskEmail(widget.userEmail),
                  style: const TextStyle(
                      color: Colors.black,
                      fontWeight: FontWeight.bold,
                      fontSize: 15)),
            ],
            style: const TextStyle(color: Colors.black54, fontSize: 15)),
        textAlign: TextAlign.center,
      ),
    );
  }


  Widget _resendCode(){
    return RichText(
      textAlign: TextAlign.center,
      text: TextSpan(
          text: "Didn't receive the code? ",
          children: [
            WidgetSpan(
              child: Center(
                child: InkWell(
                  onTap: () {
                    textEditingController.clear();

                    print("resent code");
                    setState(() {
                      currentText = "";
                      hasError = false;
                      timeExpiration = false;
                    });
                  },
                  child: const Text(
                    "Resend code",
                    style: TextStyle(
                        color: optTextColor,fontWeight: FontWeight.bold,fontSize: 13),
                  ),
                ),
              ),
            )
          ]),
    );
  }



}

