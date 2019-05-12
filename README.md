# Na tropie
Branded app for polish scout magazine

## Getting Started 

If you would like to run project in your Android Studio, follow steps:
1. create app/src/main/res/drawable/nt_logo.png
2. create app/src/main/res/drawable/nt_small_logo.png
3. insert own app/google-services.json from firebase
4. create app/src/main/res/values/settings.xml (see template)
5. create app/src/main/res/values/nt_colors.xml (see template)

### settings.xml template
Provide your own api url. Url has to ends with "/".
 
```{xml}
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="NT_API_URL">http://example.com/api/</string>
</resources>
```

### nt_colors.xml
Adjust colors by your discretion.

```{xml}
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="row_post_title">#FFFFFF</color>
    <color name="row_post_background">#FFFFFF</color>
    <color name="app_bar_background">#FFFFFF</color>
    <color name="background">#FFFFFF</color>
    <color name="toggle_color">#FFFFFF</color>
    <color name="colorPrimary">@color/app_bar_background</color>
    <color name="colorPrimaryDark">#FFFFFF</color>
    <color name="colorAccent">#FFFFFF</color>
</resources>
```

## Important dependencies
- firebase
- google-services
- kotlinx-coroutines

## Contributing
Please read [CONTRIBUTING.md](https://github.com/writ3it/android-na-tropie/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Contributors
Thanks goes out to all people:

- ***Your name*** - some description of changes - [github username](https://github.com/writ3it)

## License
This software is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) excluding images and other graphical elements like colors which rights belongs to [NaTropie.zhp.pl](http://natropie.zhp.pl/). Protected resources are not shared in the repository. 

