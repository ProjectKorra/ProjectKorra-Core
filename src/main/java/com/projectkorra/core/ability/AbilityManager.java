package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.List;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.util.Pair;

public class AbilityManager {
	public static final List<AbilityInfo> infos = new ArrayList<>(20); 
	public static void tick() {
		long startTime = System.currentTimeMillis();
		for(BendingUser user : BendingUserManager.users) {
			user.getInstances().forEach(i -> {
				if(i.isStopped()) {
					i.stop();
				} else if (i.isStarted()) {
					i.progress();
				} else {
					i.start();
				}
			});
		}
		System.out.println("Activate Overhead in MS:" + (System.currentTimeMillis() - startTime));

	}
	
	public static void activate(BendingUser user) {
		long startTime = System.currentTimeMillis();
		if(user == null) {
			return;
		}
		
		List<Ability> top = new ArrayList<>(5);
		List<Ability> mid = new ArrayList<>(5);
		List<Ability> low = new ArrayList<>(5);

		for(Pair<AbilityInfo, Activation> e : user.getActivations()) {
			if(e.getValue().activate(user)) {
				Ability a = e.getKey().createInstance(user);
				switch(a.getPriority()) {
					case 0:
						top.add(a);
						break;
					case 2:
						low.add(a);
						break;
					case 1:
					default:
						mid.add(a);
						break;
				}
			}
		}

		if(!top.isEmpty()) {
			user.getInstances().addAll(top);
		} else if (!mid.isEmpty()) {
			user.getInstances().addAll(mid);
		} else {
			user.getInstances().addAll(low);
		}

		System.out.println("Activate Overhead in MS:" + (System.currentTimeMillis() - startTime));
	}
}
