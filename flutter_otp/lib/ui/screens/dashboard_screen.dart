import 'package:flutter/material.dart';


BoxDecoration userAvatarDecoration = BoxDecoration(
    shape: BoxShape.circle,
    color: Colors.grey.shade200,
    boxShadow: const [
      BoxShadow(
        color: Colors.white,
        offset: Offset(10, 10),
        blurRadius: 10,
      ),
      BoxShadow(
        color: Colors.white,
        offset: Offset(-10, -10),
        blurRadius: 10,
      ),
    ]
);

class DashboardScreen extends StatelessWidget {
  static const String routeName = "/dashboard";

  const DashboardScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body:Center(child:
        Stack(
          children: <Widget>[
            Column(
              children: const <Widget>[
                AvatarImage(),
                SizedBox(
                  height: 30,),
                Text(
                  'Welcome / Bienvenido',
                  style: TextStyle(
                      fontSize: 30,
                      fontWeight: FontWeight.w700,
                      fontFamily: "Poppins"),
                ),
                Text(
                  '@boottech',
                  style: TextStyle(fontWeight: FontWeight.w300),
                ),
                SizedBox(height: 15),
                Text(
                  'I am Software Engineer',
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: 20, fontFamily: "Poppins"),
                ),
                SizedBox(height: 40),
                Card(
                  child: ListTile(
                    title: Center(child: Text("You have successfully logged in", style: TextStyle(color: Colors.white),),),
                  ),
                  margin: EdgeInsets.all(30),
                  color: Colors.green,
                )
              ],
            ),
          ],
        ),)
    );
  }

}
class AvatarImage extends StatelessWidget {
  const AvatarImage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 150,
      height: 150,
      padding: const EdgeInsets.all(8),
      decoration: userAvatarDecoration,
      child: Container(
        decoration: userAvatarDecoration,
        padding: const EdgeInsets.all(3),
        child: Container(
          decoration: const BoxDecoration(
            shape: BoxShape.circle,
            image: DecorationImage(
              image: AssetImage('assets/images/avatar.png'),
            ),
          ),
        ),
      ),
    );
  }
}
