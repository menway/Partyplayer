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

### Technical, Copyright and License ###

For copyright notice see NOTICE

PartyPlayer is written in Java and is released under the GNU General Public License (GPL) (see LICENSE)
for details). It is dependent of following 3rd party libraries thus far:

-   [h2](http://www.h2database.com/html/main.html)
-   [slf4j](http://www.slf4j.org)
-   [xuggler](http://www.xuggle.com/xuggler/)
-   [jlhttp](http://www.freeutils.net/source/jlhttp/)

Because of size issues with compiled versions and dependency issues with sources
this repository doesn't contain any of them. If you want to compile this project
you have to obtain and add the libraries yourself.
