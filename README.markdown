PartyPlayer
-----------

### Please note: ###
PartyPlayer is still in an early development state and is not yet
functional. Feel free to contact me, if you want to contribute. I would
gladly accept additional members to the development team. Translators are
also needed.

### What is PartyPlayer? ###

PartyPlayer is a plattform independent queue-based media player that comes
in handy at partys and pubs, where one wants to give guests the possibility
to choose the music that is played.

The player plays music from a FIFO (first in first out) queue. Anyone, who
has access to the Webinterface can add songs to the queue. Only users with
admin rights can change that behavior (e.a. immediatly play a song).

When there is no queue or it runs out, the player starts playing songs from
intelligent playlists.

Intelligent playlists can also be used to blacklist songs, so that they won't
be played, even if they were qued.

When a user searches for songs, the internal library is first searched through.
If a song matching the search criteria cannot be found, the search continues
on YouTube. So it will be possible to add songs from youtube.

The player will support at least the following audio formats:
mp3, ogg and flac

### Technical ###

PartyPlayer is written in Java and is released under the LGPL (see LICENSE.txt
for details). It uses thus far following 3rd party libraries:

-   [JavaLayer](http://www.javazoom.net/javalayer/javalayer.html)
-   [h2](http://www.h2database.com/html/main.html)
-   [Tritonus](http://www.tritonus.org/)