<b>Add it in your project :</b>

<b>STEP 1 :</b> 

Add it in your root build.gradle at the end of repositories

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  STEP 2 : Add the dependency
  
  dependencies {
	        implementation 'com.github.anandsingh2903:Blurryit:v1.0'
	}

