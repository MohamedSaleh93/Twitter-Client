# Twitter-Client

It is a simple twitter app called TwittyFollowers

## Getting Started

The app main purpose is making user login with his twitter account then app will show all user followers
with follower Image, name, handle and bio if exists.
If you clicked on any follower on the list the app will open new page contain follower name and last
five tweets of this follower
App is in two languages English (as a main language) and arabic

## References

* At the first I depended on twitter kit to add login in the app
* Then used Twitter4J library to make a connection to twitter service to get the followers
    http://twitter4j.org/en/index.html
* when I was designing the app I used the support library V 7 for main follower list by using RecyclerView
* the creation of list reference was
    http://www.devexchanges.info/2017/02/android-recyclerview-dynamically-load.html
* For all images stream in the app I used Picasso Library
    http://square.github.io/picasso/
