/* Scene Manager
Brett Binnersley, V00776751

Manages Scenes and transitions between them.
*/

import java.util.ArrayList;

class SceneManager {
  // Initalize the scene manager
  public static void Initialize(String init_scene) {
    Scene.Initialize();
    switch_scene = init_scene.toLowerCase();
    switchObject = null;
    if (init_scene != null) {
      LoadSceneObjects();
    } else {
      System.out.println("Error: No initial scene found");
    }
  }

  // Is the scene being changed after this frame ??
  public static boolean SwitchingScene() {
    return switch_scene != null;
  }

  // Actually set the scene
  public static void SetScene(String target) {
    switch_scene = target.toLowerCase();
    switchObject = new SceneTransition(0, 0, 0);
    Scene.AddObject(switchObject);
  }

  // This should never be called by anything other than the GameLoop.
  // Returns true if the scene did change. Returns false if the scene didn't
  public static boolean RunSceneChangeFromGameLoop() {
    if (switch_scene != null) {
      if (switchObject.IsDoneTransition()) {
        LoadSceneObjects();
        return true;
      }
    }
    return false;
  }


  /////////////////////
  /////////////////////
  // Private Methods //
  /////////////////////
  /////////////////////

  private static void LoadSceneObjects() {
    ViewPort.SetFollowObject(null);  // Update ViewPort
    Scene.ClearSceneObjects();  // Re-create the scene (deletes objects in it)
    ArrayList<GameObject> objects = null;
    switch (switch_scene) {
      case "mainmenu":
        objects =  SceneCreator.CreateMainMenu();
      break;

      case "menucredits":
        objects =  SceneCreator.CreateCreditsMenu();
      break;

      case "menucontrols":
        objects =  SceneCreator.CreateControlsMenu();
      break;

      case "room1":
        objects =  SceneCreator.CreateRoom1();
      break;

      case "gameover":
        objects = SceneCreator.CreateGameOver();
      break;

      default:
        System.out.println(switch_scene);
        System.out.println("Error: Unknown Target Scene");
        return;
    }

    // Enter a new scene (fade in transition)
    objects.add(new SceneEntry(0, 0, 0));

    // Add the new objects
    Scene.AddObjects(objects);
    Scene.AddQueuedObjectsToScene();

    // Switch to this scene
    switch_scene = null;
  }

  // Variables for the scene manager
  private static String switch_scene;
  public static SceneTransition switchObject;
}
