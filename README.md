# Asteroid Radar

This is a app demo where it shows asteroids near the earth and its detail. 
I used the follows libs and plugins:

Retrofit library to download the data from the Internet.
Moshi to convert the JSON data we are downloading to usable data in the form of custom classes.
Picasso library to download and cache images (You could also use Glide, but we found it has some issues downloading images from this particular API).
RecyclerView to display the asteroids in a list.

The Jetpack library:

ViewModel
Room
LiveData
Data Binding
Navigation


#IMPORTANT
You should insert a nasa api key in `build.gradle(:app)` > `buildType` section >  `API_KEY`  "\"----Your https://api.nasa.gov API key-----\""

