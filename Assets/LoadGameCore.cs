using UnityEngine;
using clojure.lang;

public class LoadGameCore : MonoBehaviour {

	void Start () {
		RT.load("game/core");		
	}

}
