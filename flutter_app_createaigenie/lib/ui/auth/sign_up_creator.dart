import 'package:create_ai_genie/ui/auth/login.dart';
import 'package:create_ai_genie/ui/common_widgets/round_button.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import 'package:google_fonts/google_fonts.dart';
import 'dart:io';

import '../../utils/constants/app_spacer_constants.dart';
import '../../utils/constants/scale_factor.dart';
import '../home_screen/home.dart';

class SignUpCreator extends StatefulWidget {
  const SignUpCreator({super.key});

  @override
  State<SignUpCreator> createState() => _SignUpCreatorState();
}

class _SignUpCreatorState extends State<SignUpCreator> {
  bool showProgress = false;
  bool visible = false;

  final _formkey = GlobalKey<FormState>();

  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmpassController =
  TextEditingController();
  final TextEditingController name = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController bioController = TextEditingController();
  final TextEditingController weight = TextEditingController();
  bool _isObscure = true;
  final bool _isObscure2 = true;
  File? file;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: (){Navigator.pop(context);},

          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(
          left: 20,
          right: 20,
        ),
        child: CustomScrollView(
          slivers: [
            SliverFillRemaining(
                hasScrollBody: false,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const SizedBox(
                          width: double.infinity,
                        ),
                        Text(
                          'Creat AI Genie',
                          style: context.displayMedium,
                          textScaler: TextScaler.linear(
                            ScaleSize.textScaleFactor(context),
                          ),
                          maxLines: 1,
                        ),
                        AppSpacer.p10(),
                        Text(
                          'Please fill in the details ! ',
                          style: context.displayLarge,
                        ),
                        Stack(
                          children: [
                            SizedBox(
                                height: 90,
                                child: Image.asset(
                                    'assets/icons/sign_person.png')),
                            Positioned(
                                right: 0,
                                bottom: -1,
                                child:
                                Image.asset('assets/icons/sign_add.png')),
                          ],
                        ),
                        Form(
                          key: _formkey,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Full Name',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: name,
                                    decoration: InputDecoration(
                                      hintText: 'Enter your Name',
                                      hintStyle:
                                      const TextStyle(color: Color(0xffBABABA)),
                                      enabled: true,
                                      contentPadding: const EdgeInsets.only(
                                          left: 14.0, bottom: 8.0, top: 8.0),
                                      focusedBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Name cannot be empty";
                                      }
                                      if (!RegExp(r'^[a-zA-Z]+$')
                                          .hasMatch(value)) {
                                        return ("Please enter a valid Name only Alphabets");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Email Id',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: emailController,
                                    decoration: const InputDecoration(
                                      hintText: 'Enter your email id',
                                      enabled: true,
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Email cannot be empty";
                                      }
                                      if (!RegExp(
                                          "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-z]")
                                          .hasMatch(value)) {
                                        return ("Please enter a valid email");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Phone Number',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: mobileController,
                                    decoration: const InputDecoration(
                                      hintText: 'Enter your phone number',
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Number cannot be empty";
                                      }
                                      if (!RegExp(r'^[0-9]+$')
                                          .hasMatch(value)) {
                                        return ("Please enter a valid number only numeric");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.number,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Password',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    obscureText: _isObscure,
                                    controller: passwordController,
                                    decoration: InputDecoration(
                                      suffixIcon: IconButton(
                                          icon: Icon(_isObscure
                                              ? Icons.visibility_off
                                              : Icons.visibility),
                                          onPressed: () {
                                            setState(() {
                                              _isObscure = !_isObscure;
                                            });
                                          }),
                                      hintText: 'Enter a strong password',
                                    ),
                                    validator: (value) {
                                      RegExp regex = RegExp(r'^.{6,}$');
                                      if (value!.isEmpty) {
                                        return "Password cannot be empty";
                                      }
                                      if (!regex.hasMatch(value)) {
                                        return ("please enter valid password min. 6 character");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Your Bio',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: bioController,
                                    decoration: InputDecoration(
                                      hintText: 'Tell us about yourself (optional)',
                                      hintStyle:
                                      const TextStyle(color: Color(0xffBABABA)),
                                      enabled: true,
                                      contentPadding: const EdgeInsets.only(
                                          left: 14.0, bottom: 8.0, top: 8.0),
                                      focusedBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                    ),

                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              )
                            ],
                          ),
                        ),
                      ],
                    ),
                    const Spacer(),
                    Column(
                      children: [
                        RoundButton(
                            text: 'Sign Up',
                            voidCallBack: () {
                              if (_formkey.currentState!.validate()) {
                                Navigator.push(context, MaterialPageRoute(builder: (context) => const Home(),));

                              }
                            }),
                        AppSpacer.p10(),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text('Already have an account ?',
                                style: GoogleFonts.poppins(
                                  textStyle: const TextStyle(
                                    fontFamily: 'Poppins',
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14.0,
                                    height: 1.25,
                                    // Corresponds to line height of 17.5px (17.5px / 14px)
                                    letterSpacing: -0.01,
                                    // -1% of 14px
                                    color: Colors.black,
                                  ),
                                )),
                            TextButton(
                                onPressed: () {
                                  Navigator.push(context, MaterialPageRoute(builder: (context) => const Login(),));
                                },
                                child: const Text(
                                  'Log In',
                                  style: TextStyle(color: Color(0xffB219F0)),
                                )),
                          ],
                        ),
                        const SizedBox(
                          height: 20,
                        )
                      ],
                    )
                  ],
                ))
          ],
        ),
      ),
    );
  }
}
