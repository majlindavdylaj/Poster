# Poster

Poster is an Android Library for manipulation with image and text.

## Installation

Add it in your root build.gradle at the end of repositories:

```bash
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency

```bash
dependencies {
	        implementation 'com.github.koddevyoutube:poster:1.2.0'
	}
```

## Usage

```xml
<com.koddev.poster.Poster
        android:id="@+id/poster"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

```java
Poster poster;
...
poster = findViewById(R.id.poster);
poster.setDeleteButtonPosition(Position.TOP_RIGHT);
...
poster.addImage(R.mipmap.ic_launcher)
           .setBackgroundColor(Color.BLUE)
           .setAlpha(1f)
           .setScaleType(ImageView.ScaleType.CENTER_INSIDE);

poster.addText("Poster")
           .setTextColor(Color.BLUE)
           .setGravity(Gravity.CENTER)
           .setAlpha(0.8f)
           .setAllCaps(false)
           .setBackgroundColor(Color.GREEN);
```

```java
poster.setOnCurrentView(new Poster.OnCurrentView() {
            @Override
            public void currentView(View currentView) {
                if (currentView instanceof TextView){
                    // TextView
                    Toast.makeText(MainActivity.this, "text", Toast.LENGTH_SHORT).show();
                } else if (currentView instanceof ImageView){
                    // ImageView
                    Toast.makeText(MainActivity.this, "image", Toast.LENGTH_SHORT).show();
                } else {
                    // View is null
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDelete() {
                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
            }
        });
```

## Screenshots

![Alt text](https://github.com/KODDevYouTube/Poster/blob/master/Screenshots/d1.png?raw=true)
![Alt text](https://github.com/KODDevYouTube/Poster/blob/master/Screenshots/d2.png?raw=true)
![Alt text](https://github.com/KODDevYouTube/Poster/blob/master/Screenshots/d3.png?raw=true)
