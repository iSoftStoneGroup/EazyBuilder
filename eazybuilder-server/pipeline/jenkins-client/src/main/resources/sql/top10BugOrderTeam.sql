select rs.total, rs.name as projectName,ct.name as teamName from (SELECT sum(cm.val) as total , project.name as name, project.id as id from ci_metric cm
left join ci_pipeline_history cp  on cm.pipeline_id  = cp.id
left JOIN  ci_project project on  cp.project_id  = project.id
where cm.type in (%s) GROUP  by project.id  order by total desc limit 10) AS  rs
left join ci_project   on rs.id = ci_project.id
left JOIN ci_team ct  on ct.id  = ci_project.team_id