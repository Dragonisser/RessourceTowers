package de.prwh.ressourcetowers.main;

public enum RTPermissions {
	RessourceTowers("ressourcetowers"), AddTower("ressourcetowers.addtower"), SpawnTower("ressourcetowers.spawntower"), RemoveTower("ressourcetowers.removetower"), ListTower("ressourcetowers.listtower"), ChunkTower(
			"ressourcetowers.chunktower"), SaveTowerList("ressourcetowers.savetowerlist"), ReloadConfig("ressourcetowers.reloadtowerconfig"), EditTower("ressourcetowers.edittower");

	private String permissionsName = "";

	RTPermissions(String name) {
		permissionsName = name;
	}

	public String getPermissionName() {
		return permissionsName;
	}
}
