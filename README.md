# WhereToEat? - Application in android :fork_and_knife:
## An application for foodies, who can't decide where to eat, especially in the USA, to find their happy place :fries: :hamburger: :pizza: :statue_of_liberty: :arrow_down_small: ##
This app greets you with a colorful and appetizing splash screen logo, then you need to log in, and you can view a list of restaurants. The list can be filtered by cities, countries, and pricing, but you can also search in a current list, by name.
as you press the star icon at a list element, that will be stored in the database, associated with your name, for future reccurence.
By clicking on a specific restaurant, you can view its details, call the place, reserve by clicking the URL, and also view location on map. There is also an opportunity to upload your own picture from the gallery, to the restaurants, as the pictures stored in the API are not very specific, neither colorful.
By visiting the Profile section, through the bottom menu, you will see your registered data, and you have a possibility to upload your own profile picture, which will be loaded there in a centercrop view. At the right upper corner, on the appbar you get a sign out button, if you sign out, you will be redirected to the sign-in screen.
Also on your profile, you will get a favorites button, by pressing it, you will see a specific list, containing your specially favorited restaurants. By tapping them on a long-press, you will be prompted if you want to delete it from favorites, also if you want to clean all of your fav list, there is an opportunity for that too, a bin icon in the right upper corner of the screen.
- - - - - - - - - - - - - - - - - - 
## Technical details ##

   Component  | Usage
------------- | -------------
Activity | Only 1 main
Fragments | 7 for different screens
Constraint layout | All the project
Recyclerview | Listed and favorited restaurants
Material CardView  | Displaying restaurants
Glide  | Displaying images
Retrofit | Getting data from the API
Room | Storing favorites and userdata
Livedata | Controlling data from room
Navigation | Whole app
____________________________________________________________________________________________________________________________________
# Used API link #

[API Restaurants](https://ratpark-api.imok.space/)

# All rights reserved #
> :copyright: Moldovan Adrienn, Sapientia Hungarian University of Transylvania
## Contacts ##
<moldovan.adrienn17@gmail.com>

## Contributors ##
- Moldovan Adrienn | [git profile](https://github.com/madrienn17)
