import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const channel = const MethodChannel('posts_demo/api_channel');

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: UsersPage(title: 'Users'),
    );
  }
}

class UsersPage extends StatefulWidget {
  UsersPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _UsersPageState createState() => _UsersPageState();
}

class _UsersPageState extends State<UsersPage> {
  var colorsPointer = -1;

  var colors = [
    Colors.green,
    Colors.deepOrange,
    Colors.blue,
    Colors.orange,
    Colors.blueGrey,
    Colors.red,
    Colors.purple,
    Colors.pink,
  ];

  List<User> _users = List();

  Future<void> _getAllUsers() async {
    List<String> result = await channel.invokeListMethod('getAllUsers');
    List<User> users = result.map((s) {
      return User.fromJson(jsonDecode(s));
    }).toList();

    setState(() {
      _users = users;
    });
  }

  String _getInitials(String name) {
    var arr = name.split(" ");
    return "${arr[0][0]}${arr[1][0]}";
  }

  Color _getColor() {
    colorsPointer = (colorsPointer + 1) % colors.length;
    return colors[colorsPointer];
  }

  void _onUserSelected(User user) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => PostsPage(
                  title: "${user.name.split(" ")[0]}'s Posts",
                  userId: user.id,
                )));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: ListView.builder(
          itemCount: _users.length,
          itemBuilder: (context, position) {
            return ListTile(
              onTap: () {
                _onUserSelected(_users[position]);
              },
              leading: CircleAvatar(
                backgroundColor: _getColor(),
                child: Text(
                  _getInitials(_users[position].name),
                  style: TextStyle(color: Colors.white),
                ),
              ),
              title: Text(_users[position].name),
            );
          },
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _getAllUsers();
  }
}

class PostsPage extends StatefulWidget {
  PostsPage({Key key, this.title, this.userId}) : super(key: key);

  final String title;
  final int userId;

  @override
  _PostsPageState createState() => _PostsPageState();
}

class _PostsPageState extends State<PostsPage> {
  List<Post> _posts = List();

  Future<void> _getPostsForUser(int userId) async {
    List<String> result =
        await channel.invokeListMethod('getPostsForUser', {"userId": userId});
    List<Post> posts = result.map((s) {
      return Post.fromJson(jsonDecode(s));
    }).toList();

    setState(() {
      _posts = posts;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: ListView.separated(
          padding: EdgeInsets.only(top: 10.0, bottom: 10.0),
          separatorBuilder: (context, index) => Divider(),
          itemCount: _posts.length,
          itemBuilder: (context, position) => ListTile(
                title: Padding(
                  padding: EdgeInsets.only(bottom: 8.0),
                  child: Text(
                    _posts[position].title,
                    style:
                        TextStyle(fontWeight: FontWeight.w600, fontSize: 18.0),
                    softWrap: true,
                  ),
                ),
                subtitle: Text(
                  _posts[position].body,
                  softWrap: true,
                ),
              ),
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _getPostsForUser(widget.userId);
  }
}

class User {
  final int id;
  final String name;

  User(this.id, this.name);

  User.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        name = json['name'];
}

class Post {
  final int id;
  final String title;
  final String body;

  Post(this.id, this.title, this.body);

  Post.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        title = json['title'],
        body = json['body'];
}
