package com.eazybuilder.ci.entity;

/**
 * k8s b部署类型
 */
public enum AppType {
	/**
	 *  prod 类型为deployment
	 */
	deployment,
	/**
	 *  prod statefulset
	 */
	statefulset,
	/**
	 *  prod configmap
	 */
	configmap,
	/**
	 *  prod 类型为job
	 */
	job
}
