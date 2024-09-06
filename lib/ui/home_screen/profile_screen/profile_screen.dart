import 'package:aigeniog/ui/common_widgets/round_button.dart';
import 'package:aigeniog/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import 'dart:io';

import '../../../utils/constants/app_spacer_constants.dart';
import '../../../utils/constants/scale_factor.dart';
import '../home.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  bool showProgress = false;
  bool visible = false;

  final _formKey = GlobalKey<FormState>();

  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmpassController = TextEditingController();
  final TextEditingController name = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController hobbiesController = TextEditingController();
  final TextEditingController interestsController = TextEditingController();
  final TextEditingController weight = TextEditingController();
  bool _isObscure = true;
  File? file;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: () {
            Navigator.pop(context);
          },
          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
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
                      const SizedBox(width: double.infinity),
                      Text(
                        'Create AI Genie',
                        style: context.displayMedium,
                        textScaler: TextScaler.linear(
                          ScaleSize.textScaleFactor(context),
                        ),
                        maxLines: 1,
                      ),
                      AppSpacer.p10(),
                      Text(
                        'Your Profile !',
                        style: context.displayLarge,
                      ),
                      SizedBox(
                        height: 90,
                        child: Image.asset('assets/icons/sign_person.png'),
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text('Use Personalized Profile ', style: context.displaySmall),
                          AppSpacer.p8(),
                          Image.asset('assets/icons/s_switch_icon.png')
                        ],
                      ),
                      Form(
                        key: _formKey,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            _buildTextField(
                              label: 'Full Name',
                              controller: name,
                              hintText: 'Name',
                              validator: (value) {
                                if (value!.isEmpty) {
                                  return "Name cannot be empty";
                                }
                                if (!RegExp(r'^[a-zA-Z]+$').hasMatch(value)) {
                                  return "Please enter a valid name with alphabets only";
                                }
                                return null;
                              },
                            ),
                            _buildTextField(
                              label: 'Email Id',
                              controller: emailController,
                              hintText: 'user@gmail.com',
                              validator: (value) {
                                if (value!.isEmpty) {
                                  return "Email cannot be empty";
                                }
                                if (!RegExp(r"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-z]").hasMatch(value)) {
                                  return "Please enter a valid email";
                                }
                                return null;
                              },
                            ),
                            _buildTextField(
                              label: 'Phone Number',
                              controller: mobileController,
                              hintText: '980000000',
                              validator: (value) {
                                if (value!.isEmpty) {
                                  return "Number cannot be empty";
                                }
                                if (!RegExp(r'^[0-9]+$').hasMatch(value)) {
                                  return "Please enter a valid number";
                                }
                                return null;
                              },
                              keyboardType: TextInputType.number,
                            ),
                            _buildTextField(
                              label: 'Password',
                              controller: passwordController,
                              hintText: '*********',
                              obscureText: _isObscure,
                              validator: (value) {
                                if (value!.isEmpty) {
                                  return "Password cannot be empty";
                                }
                                if (value.length < 6) {
                                  return "Password must be at least 6 characters";
                                }
                                return null;
                              },
                              suffixIcon: IconButton(
                                icon: Icon(
                                  _isObscure ? Icons.visibility_off : Icons.visibility,
                                ),
                                onPressed: () {
                                  setState(() {
                                    _isObscure = !_isObscure;
                                  });
                                },
                              ),
                            ),
                            _buildTextField(
                              label: 'Your Hobbies',
                              controller: hobbiesController,
                              hintText: 'Tell us your hobbies',
                            ),
                            _buildTextField(
                              label: 'Your Interests',
                              controller: interestsController,
                              hintText: 'Tell us your interests',
                            ),
                            const SizedBox(height: 30),
                          ],
                        ),
                      ),
                    ],
                  ),
                  const Spacer(),
                  Column(
                    children: [
                      RoundButton(
                        text: 'Save',
                        voidCallBack: () {
                          if (_formKey.currentState!.validate()) {
                            Navigator.push(
                              context,
                              MaterialPageRoute(builder: (context) => const Home()),
                            );
                          }
                        },
                      ),
                      AppSpacer.p10(),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTextField({
    required String label,
    required TextEditingController controller,
    required String hintText,
    bool obscureText = false,
    TextInputType keyboardType = TextInputType.text,
    Widget? suffixIcon,
    String? Function(String?)? validator,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),
        ),
        TextFormField(
          controller: controller,
          decoration: InputDecoration(
            hintText: hintText,
            hintStyle: const TextStyle(color: Color(0xffBABABA)),
            enabled: true,
            contentPadding: const EdgeInsets.symmetric(horizontal: 14.0, vertical: 8.0),
            focusedBorder: OutlineInputBorder(
              borderSide: const BorderSide(color: Color(0xffCBD2E0)),
              borderRadius: BorderRadius.circular(8),
            ),
            enabledBorder: OutlineInputBorder(
              borderSide: const BorderSide(color: Color(0xffCBD2E0)),
              borderRadius: BorderRadius.circular(8),
            ),
            suffixIcon: suffixIcon,
          ),
          obscureText: obscureText,
          keyboardType: keyboardType,
          validator: validator,
        ),
        const SizedBox(height: 20),
      ],
    );
  }
}
