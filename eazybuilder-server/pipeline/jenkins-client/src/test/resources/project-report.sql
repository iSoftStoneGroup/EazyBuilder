
select distinct t1.project_name,t1.name as team,t1.lastbuild_status,t1.lastbuild_time,t2.lastsuccess,
max(case t2.type when 'dc_high' then t2.val else 0 end) dc_high,
max(case t2.type when 'dc_medium' then t2.val else 0 end) dc_medium,
max(case t2.type when 'bug_blocker' then t2.val else 0 end) bug_blocker,
max(case t2.type when 'bug_critical' then t2.val else 0 end) bug_critical,
max(case t2.type when 'vulner_blocker' then t2.val else 0 end) vulner_blocker,
max(case t2.type when 'vulner_critical' then t2.val else 0 end) vulner_critical,
max(case t2.type when 'code_smell_blocker' then t2.val else 0 end) code_smell_blocker,
max(case t2.type when 'code_smell_critical' then t2.val else 0 end) code_smell_critical,
max(case t2.type when 'unit_test_coverage_rate' then t2.val else 'n/a' end) unit_test_coverage_rate,
max(case t2.type when 'code_line' then t2.val else 0 end) code_line
from(
SELECT 
    a.id,a.project_name,t.name,a.status as lastbuild_status,a.start_time_millis as lastbuild_time
FROM
    ci_pipeline_history a,
    ci_project p,
    ci_team t
WHERE
    p.id=a.project_id
    and p.team_id=t.id
    and 
    a.start_time_millis IN (SELECT 
            MAX(b.start_time_millis) AS lastbuild
        FROM
            ci_pipeline_history b
        GROUP BY b.project_id)
    and a.project_name like 'NHSK_%'
) t1 left join 
(
SELECT 
    a.id,a.project_name,t.name,a.status,a.start_time_millis as lastsuccess,m.type,m.val
FROM
    ci_pipeline_history a,
    ci_project p,
    ci_team t,
    ci_metric m
WHERE
    p.id=a.project_id
    and p.team_id=t.id
    and m.pipeline_id=a.id
    and 
    a.start_time_millis IN (SELECT 
            MAX(b.start_time_millis) AS lastbuild
        FROM
            ci_pipeline_history b
        GROUP BY b.project_id)
    and a.project_name like 'NHSK_%'
    and a.status=2 or a.status=6

) t2 on t1.id=t2.id  

group by project_name,team,lastbuild_status,lastbuild_time,lastsuccess


