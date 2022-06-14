import 'package:flutter/material.dart';
import 'package:flutter_otp/core/constants/color.dart';
import 'package:flutter_otp/core/constants/text_styles.dart';

class CustomFormField extends StatefulWidget {
  final String headingText;
  final String hintText;
  final bool obscureText;
  final Widget suffixIcon;
  final TextInputType textInputType;
  final TextInputAction textInputAction;
  final TextEditingController controller;
  final int maxLines;
  final FormFieldValidator? validator;

  const CustomFormField(
      {Key? key,
        required this.headingText,
        required this.hintText,
        required this.obscureText,
        required this.suffixIcon,
        required this.textInputType,
        required this.textInputAction,
        required this.controller,
        required this.maxLines, this.validator})
      : super(key: key);

  @override
  State<CustomFormField> createState() => CustomFormFieldState();
}

class CustomFormFieldState extends State<CustomFormField> {
  String? error;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          margin: const EdgeInsets.only(
            left: 20,
            right: 20,
            bottom: 10,
          ),
          child: Text(
            widget.headingText,
            style: CustomTextStyle.textFieldHeading,
          ),
        ),
        Container(
          margin: const EdgeInsets.only(left: 20, right: 20),
          decoration: BoxDecoration(
            color: grayShade,
            borderRadius: BorderRadius.circular(15),
          ),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12.0),
            child: TextField(
              maxLines: widget.maxLines,
              controller: widget.controller,
              textInputAction: widget.textInputAction,
              keyboardType: widget.textInputType,
              obscureText: widget.obscureText,
              decoration: InputDecoration(
                  hintText: widget.hintText,
                  hintStyle: CustomTextStyle.textFieldHintStyle,
                  border: InputBorder.none,
                  suffixIcon: widget.suffixIcon),
            ),
          ),
        ),
        const SizedBox(
          height: 5,
        ),
        error == null
            ? Container()
            : Padding(
            padding: const EdgeInsets.symmetric(horizontal: 15.0), child: Text(
            error!,
            style: Theme.of(context).textTheme.bodyText2!.copyWith(color: Theme.of(context).errorColor,fontWeight: FontWeight.normal)
        ))
      ],
    );
  }

  String? validate() {
    setState(() {
      error = widget.validator!(widget.controller.text);
    });
    return error;
  }
}