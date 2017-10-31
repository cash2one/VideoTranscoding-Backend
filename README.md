# VideoTranscoding
[![Build Status](https://travis-ci.com/luiscajl/VideoTranscoding.svg?token=pmaXrqcdKzZPYdpspVgq&branch=master)](https://travis-ci.com/luiscajl/VideoTranscoding)
![Version](https://img.shields.io/badge/version-0.1-brightgreen.svg?style=flat)

This application transcode a video that you send on all formats what you want and diferent resolutions.


###### Everything seems to be working nice now
Mac is a whatsapp bot/framework I made as a weekend project. The project itself has all you need to make your own custom bot easily.

Mac has built-in human behaivor so you only have to worry about the functions you make. Every module works completely separated from the core, this means that you can erease every module and mac will keep working

_This needs **Python 3.5**_

## Develop it:
1. Clone th)
```sh
> git clone
```
2. Run setup.sh (Mosts)
```sh
> sudo ./setup.sh
```

3. Replace:
```sh
# Replace 
> XXXXXXXXX
# Replace
> XXXXXXXXX
```


4. Open **config.py** and add set your credentials

5. Ready to go! (Now you can add your own whatsapp modules)
```sh
> ./start.sh
```

## Run it
Create  inside [`modules/`](https://gr/modules) directory
```python
# mode.py

from app.mac import mac, signals

@signals.message_received.connect
def handle(message):
    #messaroperties
    if message.text == "hi":
        mac.send_message("Hello", message.conversation)
```
Now you should only add it into [`modules/__init__.py`](https://gi_.py) to enable the module
```python
# modules/__init__.py
...
from modules import hi_module
...
```
And that's it! You are ready to go.

###### If your module 
###### _You can take [`hihelp module`](https://gp.py) as an example._


## Next Fixes
The project is not submoduling yowsup now due to a lot of the modifications made are focused for this project only and to make things simpler.
- [x] Add support for @tag messages
- [x] Add support for reply messages
- [x] Add support for receiving files
- [x] Add support for big receiving big files (downloading and decryption done in chunks)
- [x] Add support for sending images
- [ ] Add support for encrypting images in chunks (_TODO_)
- [ ] Add pickle support to remember the messages when mac its turned off(_TODO_)

## Example screenshots:
![](https://xxxxxxxxxxxxxxxx.png)
![](https://xxxxxxxxxxxxxxxx.png)
![](https://xxxxxxxxxxxxxxxx.png)
<img src="https://xxxxxxxxxxxxxxxx.png" width="253px" height="450px">
<img src="https://xxxxxxxxxxxxxxxx.png" width="253px" height="450px">
<img src="https://xxxxxxxxxxxxxxxx.png" width="253px" height="450px">

###### **BTC**: 
